import { Component, OnInit, Inject, ElementRef, ViewEncapsulation } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { NestedTreeControl } from '@angular/cdk/tree';
import { MatTreeNestedDataSource } from '@angular/material/tree';
import { BehaviorSubject } from 'rxjs';
import * as _ from 'lodash';
import { DiffTreeService } from './diff-tree-modal/diff-tree.service';

export class FileNode {
  children: FileNode[];
  filename: string;
  type: any;
}


@Component({
  selector: 'app-diff-content-modal',
  templateUrl: './diff-content-modal.component.html',
  styleUrls: ['./diff-content-modal.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class DiffContentModalComponent implements OnInit {
  editorOptions = { theme: 'vs-dark', language: 'json' };

  dataChange = new BehaviorSubject<FileNode[]>([]);
  get data(): FileNode[] { return this.dataChange.value; }

  nestedTreeControl: NestedTreeControl<FileNode>;
  nestedDataSource: MatTreeNestedDataSource<FileNode>;


  constructor(
    public dialogRef: MatDialogRef<DiffContentModalComponent>,
    @Inject(MAT_DIALOG_DATA) public dataa: any,
    private treeService: DiffTreeService
  ) {
    this.nestedTreeControl = new NestedTreeControl<FileNode>(this._getChildren);
    this.nestedDataSource = new MatTreeNestedDataSource();

    this.dataChange.subscribe(data => this.nestedDataSource.data = data);
  }

  public diffObj = {};
  public jsonContent = '';
  public changments = [
    {
      methodType: '',
      url: '',
      params: [{
        type: '',
        number: 0,
        paramName: '',
        paramType: '',
        paramIn: '',

      }]
    }
  ]
  public diffContent = {
    newEndpoints: [],
    missingEndpoints: [],
    changedOperations: [],
    deprecatedEndpoints: [],
    changedEndpoints: [],
    oldVersion: '',
    newVersion: ''
  };

  hasNestedChild = (t: number, nodeData: FileNode) => !nodeData.type;

  private _getChildren = (node: FileNode) => node.children;

  ngOnInit(): void {
    this.diffObj = { ...this.dataa.diffContent };
    Object.keys(this.diffObj).forEach(type => {
      this.diffContent[type] = this.diffObj[type]
    })
    console.log('content', this.diffContent);
    this.jsonContent = JSON.stringify(this.diffContent, null, '\t');
    // Parse the string to json object.
    const dataObject = JSON.parse(JSON.stringify(this.diffContent));
    // Build the tree nodes from Json object. The result is a list of `FileNode` with nested
    //     file node as children.
    const data = this.buildFileTree(dataObject, 0);
    // Notify the change.
    this.dataChange.next(data);
    this.changments = this.checkChangedEndpoints(this.diffContent.changedEndpoints);
  }

  toUpperCase(word) {
    return _.startCase(word);
  }

  recursiveMap(endpoints) {
    return Object.keys(endpoints).reduce((accumulator, key) => {
      const value = endpoints[key];
      const node = {
        text: '',
        value: 0,
        children: []
      }
      if (value != null) {
        if (typeof value === 'object') {
          node.children = this.recursiveMap(value);
        }
        else node.children = value;
      }
      return accumulator.concat(node);
    }, []);
  }

  isObject(value) {
    return value && typeof value === 'object' && value.constructor === Object;
  }

  isNotEmpty(array) {
    return array && array.length !== 0;
  }

  schemaExist(param) {
    return param.schema !== undefined ? param.schema.type : param.type;
  }
  checkChangedEndpoints(changedEndpoints) {
    const arr = [];
    const obj = {
      methodType: '',
      url: '',
      params: [
        {
          type: '',
          number: 0,
          details: [],

        }
      ]
    };
    if (changedEndpoints.length) {
      console.log('length ', changedEndpoints.length);
      changedEndpoints.forEach(c => {
        obj.url = c.pathUrl;
        Object.keys(c.changedOperations).forEach((key) => {
          if (typeof key === 'string') {
            obj.methodType = key;
            const changed = c.changedOperations[key];
            Object.keys(changed).forEach(cKey => {
              if (Array.isArray(changed[cKey]) && changed[cKey].length > 0) {
                // obj.params.push({ type: this.toUpperCase(cKey), number: changed[cKey].length });
                changed[cKey].map(p => {
                  obj.params.forEach(s => {
                    s.type = this.toUpperCase(cKey);
                    s.details.push(p);
                  });
                })
              }
            })
            arr.push(obj);
          }
        })

      })
    }
    return arr;
  }

  changExist() {
    const arr = this.diffContent;
    return arr.changedEndpoints.length > 0 || arr.newEndpoints.length > 0 || arr.missingEndpoints.length > 0 || arr.deprecatedEndpoints.length > 0
      || arr.oldVersion !== arr.newVersion;
  }
  // Build the file structure tree. The `value` is the Json object, or a sub-tree of a Json object.
  // The return value is the list of `FileNode`.
  buildFileTree(obj: { [key: string]: any }, level: number): FileNode[] {
    return Object.keys(obj).reduce<FileNode[]>((accumulator, key) => {
      const value = obj[key];
      const node = new FileNode();
      if (value != null) {
        if (value.length !== 0) node.filename = key;
        if (typeof value === 'object') {
          node.children = this.buildFileTree(value, level + 1);
        } else {
          node.type = value;
        }
      }

      return accumulator.concat(node);
    }, []);
  }
  close() {
    this.dialogRef.close();
  }
  showDiffTree() {
    const obj = {
      nestedDataSource: this.nestedDataSource,
      treeControl: this.nestedTreeControl,
      hasChild: this.hasNestedChild
    }
    this.treeService.openDiffTree(obj);
  }
}
